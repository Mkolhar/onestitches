import { render, screen, fireEvent } from '@testing-library/react';
import ArtworkUploader from './ArtworkUploader';

beforeEach(() => {
  global.fetch = jest.fn().mockResolvedValue({ ok: true }) as any;
  global.URL.createObjectURL = jest.fn(() => 'preview-url') as any;
});

describe('ArtworkUploader', () => {
  it('shows error for unsupported file type', async () => {
    render(<ArtworkUploader />);
    const input = screen.getByTestId('file-input');
    const file = new File(['hello'], 'test.txt', { type: 'text/plain' });
    fireEvent.change(input, { target: { files: [file] } });
    expect(await screen.findByText(/unsupported file type/i)).toBeInTheDocument();
  });

  it('shows error for large files', async () => {
    render(<ArtworkUploader />);
    const input = screen.getByTestId('file-input');
    const large = new File([new ArrayBuffer(26 * 1024 * 1024)], 'large.png', { type: 'image/png' });
    fireEvent.change(input, { target: { files: [large] } });
    expect(await screen.findByText(/file too large/i)).toBeInTheDocument();
  });

  it('previews valid image', async () => {
    render(<ArtworkUploader />);
    const input = screen.getByTestId('file-input');
    const file = new File(['data'], 'img.png', { type: 'image/png' });
    fireEvent.change(input, { target: { files: [file] } });
    expect(await screen.findByAltText('preview')).toBeInTheDocument();
    expect(screen.queryByText(/unsupported/i)).toBeNull();
  });
});
