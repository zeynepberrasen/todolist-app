import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.todolist.app',
  appName: 'To-Do List',
  webDir: 'dist',
  server: {
    androidScheme: 'https',
  },
};

export default config;
