
"use client";

import Link from "next/link";
import { useCartStore } from "../../store/cart";
import { useState } from "react";

export default function CartPage() {
  const items = useCartStore((s) => s.items);
  const [clientSecret, setClientSecret] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
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
      <button
        onClick={async () => {
          try {
            setError(null);
            setClientSecret(null);
            const payload = {
              items: entries.map(([sku, item]) => ({ sku, qty: item.qty })),
            };
            const res = await fetch("http://localhost:8082/api/payments/intent", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(payload),
            });
            if (!res.ok) throw new Error(await res.text());
            const data = await res.json();
            setClientSecret(data.clientSecret);
          } catch (e: any) {
            setError(e.message ?? "Failed to create payment intent");
          }
        }}
      >
        Create PaymentIntent
      </button>
      {clientSecret && (
        <p data-testid="client-secret">client_secret: {clientSecret}</p>
      )}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </main>
  );
}
