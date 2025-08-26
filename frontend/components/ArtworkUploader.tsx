"use client";
import { useState } from "react";

/**
 * Validates artwork file type/size, uploads to backend, and shows a preview.
 */
export default function ArtworkUploader() {
  const [preview, setPreview] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    if (!["image/png", "image/jpeg", "image/svg+xml"].includes(file.type)) {
      setError("Unsupported file type");
      setPreview(null);
      return;
    }
    if (file.size > 25 * 1024 * 1024) {
      setError("File too large (25MB max)");
      setPreview(null);
      return;
    }
    setError(null);
    setPreview(URL.createObjectURL(file));
    const form = new FormData();
    form.append("file", file);
    try {
      await fetch("http://localhost:8082/api/uploads", {
        method: "POST",
        body: form,
      });
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div>
      <input
        data-testid="file-input"
        type="file"
        accept="image/*"
        onChange={handleChange}
      />
      {error && <p style={{ color: "red" }}>{error}</p>}
      {preview && <img src={preview} alt="preview" width={200} />}
    </div>
  );
}
