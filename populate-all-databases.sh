#!/bin/bash
set -e

# Pontus - Populate All Databases (Docker-native)
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; BLUE='\033[0;34m'; NC='\033[0m'

DB_USER="pontus_user"
DB_PASS="pontus_pass"

# service_name:container_name:database_name:script_path
declare -A DATABASES=(
  ["vessel"]="postgres-vessel:pontus_vessel:vessel-service/src/main/resources/data/populate_vessels.sql"
  ["dock"]="postgres-dock:pontus_dock:dock-service/src/main/resources/data/populate_dock.sql"
  ["cargo"]="postgres-cargo:pontus_cargo:cargo-service/src/main/resources/data/populate_cargo.sql"
  ["crew"]="postgres-crew:pontus_crew:crew-service/src/main/resources/data/populate_crew.sql"
  ["delivery"]="postgres-delivery:pontus_delivery:delivery-service/src/main/resources/data/populate_delivery.sql"
  ["auth"]="postgres-auth:pontus_auth:auth-service/src/main/resources/data/populate_users.sql"
)

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Pontus Database Population Script    ${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Function: wait until Postgres inside container is ready
check_postgres_ready() {
  local container=$1
  local db_name=$2
  local max_attempts=30
  local attempt=1

  echo -e "${YELLOW}Checking PostgreSQL readiness in $container...${NC}"
  while [ $attempt -le $max_attempts ]; do
    if docker exec -e PGPASSWORD=$DB_PASS "$container" pg_isready -U "$DB_USER" -d "$db_name" > /dev/null 2>&1; then
      echo -e "${GREEN}✓ $container is ready${NC}"
      return 0
    fi
    echo -e "${YELLOW}Attempt $attempt/$max_attempts: waiting for $container...${NC}"
    sleep 2
    ((attempt++))
  done
  echo -e "${RED}✗ $container not ready after $max_attempts attempts${NC}"
  return 1
}

# Execute SQL file inside container
execute_sql_script() {
  local service_name=$1
  local container=$2
  local db_name=$3
  local script_path=$4

  echo -e "${BLUE}Processing $service_name database...${NC}"
  if [ ! -f "$script_path" ]; then
    echo -e "${RED}✗ Script not found: $script_path${NC}"
    return 1
  fi

  if ! check_postgres_ready "$container" "$db_name"; then
    return 1
  fi

  echo -e "${YELLOW}Executing script inside $container...${NC}"
  cat "$script_path" | docker exec -i -e PGPASSWORD=$DB_PASS "$container" \
    psql -U "$DB_USER" -d "$db_name" > /dev/null 2>&1

  if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Successfully populated $service_name${NC}\n"
    return 0
  else
    echo -e "${RED}✗ Failed to populate $service_name${NC}\n"
    return 1
  fi
}

# --- main execution ---
echo -e "${YELLOW}Starting database population process...${NC}\n"

if ! docker ps | grep -q "postgres-"; then
  echo -e "${RED}✗ No PostgreSQL containers detected.${NC}"
  echo -e "${YELLOW}Start them with: docker compose up -d${NC}"
  exit 1
fi

echo -e "${GREEN}✓ PostgreSQL containers are running${NC}\n"

declare -A RESULTS
SUCCESSFUL=0; FAILED=0; TOTAL=${#DATABASES[@]}

for service_name in "${!DATABASES[@]}"; do
  IFS=':' read -r container db_name script_path <<< "${DATABASES[$service_name]}"
  if execute_sql_script "$service_name" "$container" "$db_name" "$script_path"; then
    RESULTS[$service_name]="SUCCESS"
    SUCCESSFUL=$((SUCCESSFUL + 1))
  else
    RESULTS[$service_name]="FAILED"
    FAILED=$((FAILED + 1))
  fi
done

# Results summary
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}           POPULATION RESULTS           ${NC}"
echo -e "${BLUE}========================================${NC}\n"

for s in "${!RESULTS[@]}"; do
  if [ "${RESULTS[$s]}" == "SUCCESS" ]; then
    echo -e "${GREEN}✓ $s: SUCCESS${NC}"
  else
    echo -e "${RED}✗ $s: FAILED${NC}"
  fi
done

echo -e "\n${BLUE}Summary:${NC}"
echo -e "${GREEN}  Successful: $SUCCESSFUL/$TOTAL${NC}"
[ $FAILED -gt 0 ] && echo -e "${RED}  Failed: $FAILED/$TOTAL${NC}"

if [ $FAILED -eq 0 ]; then
  echo -e "\n${GREEN}🎉 All databases populated successfully!${NC}"
else
  echo -e "\n${RED}⚠️  Some databases failed. See logs above.${NC}"
fi

