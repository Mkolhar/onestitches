"use client";
import { useState } from "react";
import CustomizationPreview from "../../../components/CustomizationPreview";
import ArtworkUploader from "../../../components/ArtworkUploader";
import { useCartStore } from "../../../store/cart";

interface Product {
  sku: string;
  name: string;
  category: string;
  price: number;
  imageUrl: string;
}

export default function ProductView({ product }: { product: Product }) {
  const [overlay, setOverlay] = useState<string | null>(null);
  const { setCustomization, addItem } = useCartStore((s) => ({
    setCustomization: s.setCustomization,
    addItem: s.addItem,
  }));

  const handleUploaded = (serverUrl: string, previewUrl: string) => {
    setOverlay(previewUrl);
    setCustomization(product.sku, serverUrl);
  };

  return (
    <main>
      <h1>{product.name}</h1>
      <p>Category: {product.category}</p>
      <p>₹{product.price}</p>
      <CustomizationPreview base={product.imageUrl} overlay={overlay} />
      <ArtworkUploader onUploaded={handleUploaded} />
      <button onClick={() => addItem(product.sku)}>Add to Cart</button>
    </main>
  );
}
