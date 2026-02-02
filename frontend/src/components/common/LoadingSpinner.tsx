interface Props {
  fullScreen?: boolean;
  inline?: boolean;
  size?: 'sm' | 'md' | 'lg';
}

const SIZES = {
  sm: 'h-6 w-6',
  md: 'h-12 w-12',
  lg: 'h-16 w-16',
};

export default function LoadingSpinner({ fullScreen = false, inline = false, size = 'md' }: Props) {
  const spinner = (
    <div
      className={`animate-spin rounded-full border-b-2 border-blue-600 ${SIZES[size]}`}
    />
  );

  if (inline) {
    return spinner;
  }

  if (fullScreen) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        {spinner}
      </div>
    );
  }

  return (
    <div className="flex justify-center py-20">
      {spinner}
    </div>
  );
}
