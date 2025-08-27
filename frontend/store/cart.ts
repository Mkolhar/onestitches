import { create } from "zustand";
import { persist } from "zustand/middleware";

interface CartItem {
  qty: number;
  artworkUrl?: string;
}

interface CartState {
  items: Record<string, CartItem>;
  addItem: (sku: string, qty?: number) => void;
  removeItem: (sku: string) => void;
  updateQty: (sku: string, qty: number) => void;
  setCustomization: (sku: string, artworkUrl: string) => void;
}

export const useCartStore = create<CartState>()(
  persist(
    (set) => ({
      items: {},
      addItem: (sku, qty = 1) =>
        set((state) => ({
          items: { ...state.items, [sku]: { qty, ...state.items[sku] } },
        })),
      removeItem: (sku) =>
        set((state) => {
          const { [sku]: _, ...rest } = state.items;
          return { items: rest };
        }),
      updateQty: (sku, qty) =>
        set((state) => ({
          items: { ...state.items, [sku]: { ...state.items[sku], qty } },
        })),
      setCustomization: (sku, artworkUrl) =>
        set((state) => ({
          items: {
            ...state.items,
            [sku]: { qty: state.items[sku]?.qty ?? 1, artworkUrl },
          },
        })),
    }),
    { name: "cart" }
  )
);
