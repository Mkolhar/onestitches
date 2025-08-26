"use client";

interface Props {
  base: string;
  overlay?: string | null;
}

/**
 * Displays the base product image with an optional overlay of the uploaded artwork.
 */
export default function CustomizationPreview({ base, overlay }: Props) {
  return (
    <div style={{ position: "relative", width: 400, height: 400 }}>
      <img src={base} alt="product" width={400} height={400} />
      {overlay && (
        <img
          src={overlay}
          alt="artwork-preview"
          style={{
            position: "absolute",
            top: 0,
            left: 0,
            width: "100%",
            height: "100%",
            objectFit: "contain",
            pointerEvents: "none",
          }}
        />
      )}
    </div>
  );
}
