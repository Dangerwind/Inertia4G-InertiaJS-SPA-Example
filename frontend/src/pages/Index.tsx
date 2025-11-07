import React from 'react';
import { Link } from '@inertiajs/react';

export default function Index({ description }) {
  return (
    <div className="container py-5">
      <div className="text-center mb-5">
        <h1 className="display-4 text-primary mb-3">Главная страница</h1>
        <p className="lead text-muted">{description}</p>
      </div>

      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="d-grid gap-3 d-md-flex justify-content-md-center">
            <Link href="/about" className="btn btn-primary btn-lg me-md-3">
              <i className="bi bi-info-circle me-2"></i>О нас
            </Link>
            <Link href="/products" className="btn btn-outline-secondary btn-lg">
              <i className="bi bi-list me-2"></i>Список товаров
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}