package utils

import (
	"testing"
	"time"
)

func TestCalculateScore(t *testing.T) {
	tests := []struct {
		name       string
		screenTime int64 // in milliseconds
		hour       int   // mock current hour
		wantMin    int   // minimum expected score
		wantMax    int   // maximum expected score
	}{
		{
			name:       "Morning with good behavior",
			screenTime: 30 * 60 * 1000, // 30 minutes
			hour:       8,              // 8 AM
			wantMin:    1200,
			wantMax:    2000,
		},
		{
			name:       "Afternoon with average usage",
			screenTime: 3 * 60 * 60 * 1000, // 3 hours
			hour:       14,                 // 2 PM
			wantMin:    800,
			wantMax:    1500,
		},
		{
			name:       "Evening with low usage",
			screenTime: 2 * 60 * 60 * 1000, // 2 hours
			hour:       20,                 // 8 PM
			wantMin:    1500,
			wantMax:    3000,
		},
		{
			name:       "Late night with high usage",
			screenTime: 8 * 60 * 60 * 1000, // 8 hours
			hour:       23,                 // 11 PM
			wantMin:    0,
			wantMax:    500,
		},
		{
			name:       "End of day minimal usage",
			screenTime: 1 * 60 * 60 * 1000, // Only 1 hour
			hour:       23,                 // 11 PM
			wantMin:    4000,
			wantMax:    7000,
		},
		{
			name:       "Perfect day scenario",
			screenTime: 30 * 60 * 1000, // Only 30 minutes
			hour:       23,             // End of day
			wantMin:    7000,
			wantMax:    10000,
		},
		{
			name:       "Early day excessive usage",
			screenTime: 4 * 60 * 60 * 1000, // 4 hours
			hour:       4,                  // 4 AM
			wantMin:    0,
			wantMax:    200,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Mock time.Now()
			now := time.Date(2024, 3, 20, tt.hour, 0, 0, 0, time.Local)
			originalNow := timeNow
			timeNow = func() time.Time { return now }
			defer func() { timeNow = originalNow }()

			got := CalculateScore(tt.screenTime)
			if got < tt.wantMin || got > tt.wantMax {
				t.Errorf("CalculateScore() = %v, want between %v and %v", got, tt.wantMin, tt.wantMax)
			}
		})
	}
}
