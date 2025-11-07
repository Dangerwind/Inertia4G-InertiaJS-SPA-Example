import React from 'react';
import { Link } from '@inertiajs/react';

interface Product {
  title: string;
  description: string;
}

interface Props {
  product: Product;
}

export default function ProductDetail({ product }: Props) {
  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6">
          <div className="card shadow-sm">
            <div className="card-header bg-success text-white">
              <h1 className="h4 mb-0">{product.title}</h1>
            </div>
            <div className="card-body">
              <p className="card-text">{product.description}</p>

              <div className="d-flex gap-2">
                <Link href="/products" className="btn btn-outline-secondary">
                  <i className="bi bi-arrow-left me-2"></i>К списку товаров
                </Link>
                <Link href="/" className="btn btn-outline-primary">
                  <i className="bi bi-house me-2"></i>На главную
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}