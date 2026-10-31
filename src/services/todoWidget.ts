import { registerPlugin } from '@capacitor/core';
import type { Todo } from '../types';

export interface TodoWidgetPlugin {
  syncTodos(options: { todos: Todo[] }): Promise<void>;
}

// Register custom Capacitor native plugin
export const TodoWidget = registerPlugin<TodoWidgetPlugin>('TodoWidget');

/**
 * Synchronizes the current list of Todos with the Android Home Screen Widget.
 * Works seamlessly in native Android and gracefully falls back in web environments.
 */
export async function syncTodosToWidget(todos: Todo[]): Promise<void> {
  try {
    await TodoWidget.syncTodos({ todos });
  } catch (error) {
    // If running in browser / PWA without native Capacitor runtime, quietly ignore
    console.debug('TodoWidget sync skipped (non-native environment):', error);
  }
}
