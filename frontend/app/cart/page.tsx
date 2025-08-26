"use client";

import Link from "next/link";
import { useCartStore } from "../../store/cart";

export default function CartPage() {
  const items = useCartStore((s) => s.items);
  const entries = Object.entries(items);
  
  if (entries.length === 0) {
    return (
      <main>
        <p>Your cart is empty.</p>
        <Link href="/">Continue shopping</Link>
      </main>
    );
  }
  
  return (
    <main>
      <h1>Cart</h1>
      <ul>
        {entries.map(([sku, item]) => (
          <li key={sku}>
            {sku} – Qty: {item.qty}
          </li>
        ))}
      </ul>
    </main>
  );
}
