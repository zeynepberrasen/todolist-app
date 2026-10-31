import { useState, useEffect } from 'react';
import type { Todo, FilterType } from './types';
import { TodoForm } from './components/TodoForm';
import { TodoList } from './components/TodoList';
import { TodoFilter } from './components/TodoFilter';
import { syncTodosToWidget } from './services/todoWidget';
import './App.css';

const STORAGE_KEY = 'todolist_tasks';
const THEME_KEY = 'todolist_theme_pref';

type ThemePreference = 'system' | 'light' | 'dark';

interface BeforeInstallPromptEvent extends Event {
  prompt: () => Promise<void>;
  userChoice: Promise<{ outcome: 'accepted' | 'dismissed' }>;
}

function App() {
  const [todos, setTodos] = useState<Todo[]>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      return saved ? JSON.parse(saved) : [];
    } catch {
      return [];
    }
  });
  const [currentFilter, setCurrentFilter] = useState<FilterType>('all');
  const [installPrompt, setInstallPrompt] = useState<BeforeInstallPromptEvent | null>(null);

  // Theme preference: 'system' by default (follows device setting), or manually set to 'dark' | 'light'
  const [themePref, setThemePref] = useState<ThemePreference>(() => {
    try {
      const saved = localStorage.getItem(THEME_KEY);
      return saved === 'light' || saved === 'dark' || saved === 'system' ? saved : 'system';
    } catch {
      return 'system';
    }
  });

  const [isDarkMode, setIsDarkMode] = useState<boolean>(() => {
    if (typeof window === 'undefined') return false;
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
  });

  // Apply theme and listen to system preference changes
  useEffect(() => {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');

    const applyTheme = () => {
      const isDark = themePref === 'system' ? mediaQuery.matches : themePref === 'dark';
      setIsDarkMode(isDark);
      document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');

      // Update mobile browser status bar meta tag
      const metaThemeColor = document.querySelector('meta[name="theme-color"]');
      if (metaThemeColor) {
        metaThemeColor.setAttribute('content', isDark ? '#151f32' : '#74a8c8');
      }
    };

    applyTheme();
    mediaQuery.addEventListener('change', applyTheme);
    return () => mediaQuery.removeEventListener('change', applyTheme);
  }, [themePref]);

  // Handler to toggle / cycle theme between System (Auto) -> Dark -> Light
  const handleToggleTheme = () => {
    setThemePref((current) => {
      let next: ThemePreference;
      if (current === 'system') {
        // From system, switch to opposite of current system state
        next = isDarkMode ? 'light' : 'dark';
      } else if (current === 'dark') {
        next = 'light';
      } else {
        // From light, return to system auto mode
        next = 'system';
      }

      try {
        localStorage.setItem(THEME_KEY, next);
      } catch (e) {
        console.error('Failed to save theme preference:', e);
      }
      return next;
    });
  };

  // Automatically save todos to localStorage and sync with Android Home Screen Widget
  useEffect(() => {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(todos));
    } catch (error) {
      console.error('Failed to save tasks to localStorage:', error);
    }

    // Trigger sync to Android Home Screen Widget via Capacitor plugin
    syncTodosToWidget(todos);
  }, [todos]);

  // Capture PWA install event for mobile devices
  useEffect(() => {
    const handleBeforeInstall = (e: Event) => {
      e.preventDefault();
      setInstallPrompt(e as BeforeInstallPromptEvent);
    };
    window.addEventListener('beforeinstallprompt', handleBeforeInstall);
    return () => window.removeEventListener('beforeinstallprompt', handleBeforeInstall);
  }, []);

  const handleInstallApp = async () => {
    if (!installPrompt) return;
    await installPrompt.prompt();
    const choice = await installPrompt.userChoice;
    if (choice.outcome === 'accepted') {
      setInstallPrompt(null);
    }
  };

  // Handler to add a new todo to state
  const handleAddTodo = (text: string) => {
    const newTodo: Todo = {
      id: crypto.randomUUID(),
      text,
      completed: false,
    };
    setTodos((prevTodos) => [...prevTodos, newTodo]);
  };

  // Handler to toggle todo completion status
  const handleToggleTodo = (id: string) => {
    setTodos((prevTodos) =>
      prevTodos.map((todo) =>
        todo.id === id ? { ...todo, completed: !todo.completed } : todo
      )
    );
  };

  // Handler to delete a todo
  const handleDeleteTodo = (id: string) => {
    setTodos((prevTodos) => prevTodos.filter((todo) => todo.id !== id));
  };

  // Filtered todos based on currentFilter
  const filteredTodos = todos.filter((todo) => {
    if (currentFilter === 'active') return !todo.completed;
    if (currentFilter === 'completed') return todo.completed;
    return true;
  });

  // Calculate active (uncompleted) tasks count
  const remainingCount = todos.filter((todo) => !todo.completed).length;

  return (
    <div className="app-container">
      <header className="app-header">
        <div className="app-header-top">
          <div className="app-brand">
            <img src="/app-icon.svg" alt="App Icon" className="app-logo" />
            <div className="app-brand-text">
              <h1>To-Do List</h1>
              <p className="app-subtitle">Daily Tasks & Checklist</p>
            </div>
          </div>
          <div className="app-header-actions">
            <button
              type="button"
              className="theme-toggle-btn"
              onClick={handleToggleTheme}
              title={`Theme: ${themePref === 'system' ? 'System' : themePref === 'dark' ? 'Dark' : 'Light'}. Tap to switch.`}
              aria-label="Toggle dark or light theme"
            >
              <span className="theme-toggle-icon">{isDarkMode ? '🌙' : '☀️'}</span>
              <span className="theme-toggle-label">
                {themePref === 'system' ? 'Auto' : isDarkMode ? 'Dark' : 'Light'}
              </span>
            </button>
            {installPrompt && (
              <button
                type="button"
                className="install-app-btn"
                onClick={handleInstallApp}
                title="Install as Mobile App"
              >
                📲 Install
              </button>
            )}
          </div>
        </div>
      </header>

      <main className="todo-card">
        <TodoForm onAddTodo={handleAddTodo} />
        <TodoList
          todos={filteredTodos}
          onToggle={handleToggleTodo}
          onDelete={handleDeleteTodo}
        />
        <TodoFilter
          currentFilter={currentFilter}
          remainingCount={remainingCount}
          onFilterChange={setCurrentFilter}
        />
      </main>
    </div>
  );
}

export default App;
