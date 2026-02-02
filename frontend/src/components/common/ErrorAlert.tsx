interface Props {
  message: string;
  className?: string;
}

export default function ErrorAlert({ message, className = '' }: Props) {
  if (!message) return null;

  return (
    <div
      className={`p-4 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm ${className}`}
    >
      {message}
    </div>
  );
}
