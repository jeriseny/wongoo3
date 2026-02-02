type DateFormat = 'long' | 'short' | 'datetime' | 'datetime-short';

interface FormatOptions {
  year: 'numeric' | '2-digit';
  month: 'numeric' | '2-digit' | 'long' | 'short' | 'narrow';
  day: 'numeric' | '2-digit';
  hour?: '2-digit' | 'numeric';
  minute?: '2-digit' | 'numeric';
}

const FORMAT_OPTIONS: Record<DateFormat, FormatOptions> = {
  long: {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  },
  short: {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  },
  datetime: {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  },
  'datetime-short': {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  },
};

export function formatDate(
  dateString: string | undefined,
  format: DateFormat = 'long'
): string {
  if (!dateString) return '-';
  const date = new Date(dateString);
  return date.toLocaleDateString('ko-KR', FORMAT_OPTIONS[format]);
}

export function formatDateTime(dateString: string | undefined): string {
  return formatDate(dateString, 'datetime');
}

export function formatDateShort(dateString: string | undefined): string {
  return formatDate(dateString, 'short');
}
