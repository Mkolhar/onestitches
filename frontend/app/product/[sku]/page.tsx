import { notFound } from "next/navigation";
import ProductView from "./ProductView";

type Product = {
  sku: string;
  name: string;
  category: string;
  price: number;
  imageUrl: string;
  stock: number;
};

async function getProduct(sku: string): Promise<Product | undefined> {
  const res = await fetch(`http://localhost:8081/api/inventory/products/${sku}`);
  if (!res.ok) return undefined;
  return res.json();
}

export default async function ProductPage({ params }: { params: { sku: string } }) {
  const product = await getProduct(params.sku);
  if (!product) notFound();
  return <ProductView product={product} />;
}
