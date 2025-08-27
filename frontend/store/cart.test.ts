import { useCartStore } from './cart';

describe('useCartStore', () => {
  afterEach(() => {
    useCartStore.setState({ items: {} });
  });

  it('adds and updates quantity', () => {
    const { addItem, updateQty } = useCartStore.getState();
    addItem('SKU1');
    expect(useCartStore.getState().items['SKU1'].qty).toBe(1);
    updateQty('SKU1', 3);
    expect(useCartStore.getState().items['SKU1'].qty).toBe(3);
  });

  it('removes items', () => {
    const { addItem, removeItem } = useCartStore.getState();
    addItem('SKU1');
    removeItem('SKU1');
    expect(useCartStore.getState().items['SKU1']).toBeUndefined();
  });

  it('stores customization url', () => {
    useCartStore.getState().setCustomization('SKU1', 'url');
    expect(useCartStore.getState().items['SKU1'].artworkUrl).toBe('url');
  });
});
