import { Component, OnInit } from '@angular/core';

interface ActivityItem {
  id: string;
  type: 'vessel' | 'dock' | 'cargo' | 'crew' | 'delivery';
  action: string;
  description: string;
  timestamp: Date;
  icon: string;
  color: string;
}

@Component({
  selector: 'app-recent-activity',
  templateUrl: './recent-activity.component.html',
  styleUrls: ['./recent-activity.component.scss']
})
export class RecentActivityComponent implements OnInit {
  activities: ActivityItem[] = [];
  loading = false;

  ngOnInit(): void {
    this.loadRecentActivity();
  }

  private loadRecentActivity(): void {
    // Mock data for demonstration - in real app, this would come from an API
    this.activities = [
      {
        id: '1',
        type: 'vessel',
        action: 'Vessel Arrived',
        description: 'MV Ocean Explorer has arrived at dock 3',
        timestamp: new Date(Date.now() - 15 * 60 * 1000), // 15 minutes ago
        icon: 'fas fa-ship',
        color: 'success'
      },
      {
        id: '2',
        type: 'cargo',
        action: 'Customs Cleared',
        description: 'Container cargo #CG-2024-001 cleared customs',
        timestamp: new Date(Date.now() - 45 * 60 * 1000), // 45 minutes ago
        icon: 'fas fa-check-circle',
        color: 'success'
      },
      {
        id: '3',
        type: 'crew',
        action: 'Crew Assigned',
        description: 'Captain John Smith assigned to MV Baltic Star',
        timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000), // 2 hours ago
        icon: 'fas fa-user-plus',
        color: 'info'
      },
      {
        id: '4',
        type: 'delivery',
        action: 'Delivery Scheduled',
        description: 'Inland delivery to Hamburg scheduled for tomorrow',
        timestamp: new Date(Date.now() - 3 * 60 * 60 * 1000), // 3 hours ago
        icon: 'fas fa-truck',
        color: 'primary'
      },
      {
        id: '5',
        type: 'dock',
        action: 'Dock Available',
        description: 'Dock 7 is now available for new assignments',
        timestamp: new Date(Date.now() - 4 * 60 * 60 * 1000), // 4 hours ago
        icon: 'fas fa-warehouse',
        color: 'secondary'
      }
    ];
  }

  getTimeAgo(timestamp: Date): string {
    const now = new Date();
    const diffMs = now.getTime() - timestamp.getTime();
    const diffMins = Math.floor(diffMs / (1000 * 60));
    const diffHours = Math.floor(diffMins / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (diffMins < 1) return 'Just now';
    if (diffMins < 60) return `${diffMins} minute${diffMins > 1 ? 's' : ''} ago`;
    if (diffHours < 24) return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`;
    return `${diffDays} day${diffDays > 1 ? 's' : ''} ago`;
  }
}
