import type { FilterType } from '../types';

interface TodoFilterProps {
  currentFilter: FilterType;
  remainingCount: number;
  onFilterChange: (filter: FilterType) => void;
}

export function TodoFilter({
  currentFilter,
  remainingCount,
  onFilterChange,
}: TodoFilterProps) {
  return (
    <footer className="todo-footer">
      <span className="todo-count">
        {remainingCount} {remainingCount === 1 ? 'task' : 'tasks'} remaining
      </span>

      <div className="filter-buttons">
        <button
          type="button"
          className={`filter-btn ${currentFilter === 'all' ? 'active' : ''}`}
          onClick={() => onFilterChange('all')}
        >
          All
        </button>
        <button
          type="button"
          className={`filter-btn ${currentFilter === 'active' ? 'active' : ''}`}
          onClick={() => onFilterChange('active')}
        >
          Active
        </button>
        <button
          type="button"
          className={`filter-btn ${currentFilter === 'completed' ? 'active' : ''}`}
          onClick={() => onFilterChange('completed')}
        >
          Completed
        </button>
      </div>
    </footer>
  );
}
