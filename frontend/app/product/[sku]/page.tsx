import Image from "next/image";
import { notFound } from "next/navigation";

type Product = {
  sku: string;
  name: string;
  category: string;
  price: number;
  imageUrl: string;
};

async function getProduct(sku: string): Promise<Product | undefined> {
  const res = await fetch(`http://localhost:8081/api/inventory/products/${sku}`);
  if (!res.ok) return undefined;
  return res.json();
}

export default async function ProductPage({ params }: { params: { sku: string } }) {
  const product = await getProduct(params.sku);
  if (!product) notFound();
  return (
    <main>
      <h1>{product.name}</h1>
      <p>Category: {product.category}</p>
      <p>₹{product.price}</p>
      <div style={{ width: 400, height: 400, overflow: "hidden" }}>
        <Image
          src={product.imageUrl}
          alt={product.name}
          width={400}
          height={400}
          className="zoom"
        />
      </div>
    </main>
  );
}
