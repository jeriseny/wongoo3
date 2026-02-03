import type { SearchType, SortType } from '../types';

interface Props {
  searchType: SearchType;
  keyword: string;
  sortBy: SortType;
  onSearchTypeChange: (type: SearchType) => void;
  onKeywordChange: (keyword: string) => void;
  onSortChange: (sort: SortType) => void;
  onSearch: () => void;
}

const searchTypeOptions: { value: SearchType; label: string }[] = [
  { value: 'TITLE', label: '제목' },
  { value: 'CONTENT', label: '내용' },
  { value: 'TITLE_CONTENT', label: '제목+내용' },
  { value: 'AUTHOR', label: '작성자' },
];

const sortOptions: { value: SortType; label: string }[] = [
  { value: 'LATEST', label: '최신순' },
  { value: 'VIEW_COUNT', label: '조회수순' },
];

export default function SearchBar({
  searchType,
  keyword,
  sortBy,
  onSearchTypeChange,
  onKeywordChange,
  onSortChange,
  onSearch,
}: Props) {
  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      onSearch();
    }
  };

  return (
    <div className="flex flex-col sm:flex-row gap-3 mb-6 p-4 bg-gray-50 rounded-lg">
      {/* 정렬 선택 */}
      <select
        value={sortBy}
        onChange={(e) => onSortChange(e.target.value as SortType)}
        className="px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        {sortOptions.map((opt) => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>

      {/* 검색 타입 선택 */}
      <select
        value={searchType}
        onChange={(e) => onSearchTypeChange(e.target.value as SearchType)}
        className="px-3 py-2 border border-gray-300 rounded-lg bg-white text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        {searchTypeOptions.map((opt) => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>

      {/* 검색어 입력 */}
      <div className="flex flex-1 gap-2">
        <input
          type="text"
          value={keyword}
          onChange={(e) => onKeywordChange(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="검색어를 입력하세요"
          className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button
          onClick={onSearch}
          className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition font-medium"
        >
          검색
        </button>
      </div>
    </div>
  );
}
