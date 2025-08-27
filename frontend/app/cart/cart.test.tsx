import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import CartPage from './page';
import { useCartStore } from '../../store/cart';

jest.mock('../../store/cart', () => {
  const real = jest.requireActual('../../store/cart');
  return {
    ...real,
    useCartStore: jest.fn(),
  };
});

describe('Cart checkout', () => {
  beforeEach(() => {
    (global as any).Buffer = require('buffer').Buffer;
    (global.fetch as any) = jest.fn().mockResolvedValue({ ok: true, json: async () => ({ clientSecret: 'sec_123' }) });
    (useCartStore as unknown as jest.Mock).mockReturnValue({
      items: { 'TSHIRT-001': { qty: 2 } },
    });
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  it('creates payment intent and shows client_secret', async () => {
    render(<CartPage />);
    const btn = screen.getByText(/create paymentintent/i);
    fireEvent.click(btn);
    expect(await screen.findByTestId('client-secret')).toHaveTextContent('sec_123');
  });
});


