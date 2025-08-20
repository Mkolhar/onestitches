
import Link from "next/link";

type Product = {
  sku: string;
  name: string;
  category: string;
  price: number;
  imageUrl: string;
};

async function getProducts(category?: string): Promise<Product[]> {
  const url = new URL("http://localhost:8081/api/inventory/products");
  if (category) url.searchParams.set("category", category);
  const res = await fetch(url.toString());
  if (!res.ok) throw new Error("failed to load products");
  return res.json();
}

export default async function Home({
  searchParams,
}: {
  searchParams: { category?: string };
}) {
  const products = await getProducts(searchParams.category);
  const categories = ["apparel", "outerwear", "merch"];
  return (
    <main>
      <h1>Catalog</h1>
      <nav style={{ display: "flex", gap: "1rem" }}>
        <Link href="/">All</Link>
        {categories.map((c) => (
          <Link key={c} href={`/?category=${c}`}>
            {c}
          </Link>
        ))}
      </nav>
      <ul>
        {products.map((p) => (
          <li key={p.sku}>
            <Link href={`/product/${p.sku}`}>{p.name}</Link> – ₹{p.price}
          </li>
        ))}
      </ul>
    </main>
  );
}
