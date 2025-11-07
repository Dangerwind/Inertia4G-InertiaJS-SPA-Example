import React from 'react';
import { Link } from '@inertiajs/react';

interface Product {
  id: number;
  title: string;
}

interface Pagination {
  current_page: number;
  total_pages: number;
  has_prev: boolean;
  has_next: boolean;
  prev_page: number | null;
  next_page: number | null;
}

interface Props {
  products: Product[];
  pagination: Pagination;
}

export default function ProductsList({ products, pagination }: Props) {
  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h1 className="h2 text-primary">Список товаров</h1>
            <div className="d-flex gap-2">
              <Link href="/products/create" className="btn btn-success">
                <i className="bi bi-plus-circle me-2"></i>Добавить товар
              </Link>
              <Link href="/" className="btn btn-outline-primary">
                <i className="bi bi-house me-2"></i>На главную
              </Link>
            </div>
          </div>

          {products.length === 0 ? (
            <div className="alert alert-info text-center">
              <i className="bi bi-inbox display-4 d-block mb-3"></i>
              <h5>Товаров нет</h5>
              <p className="mb-0">Здесь появятся товары, когда они будут добавлены</p>
            </div>
          ) : (
            <div className="row g-3">
              {products.map(product => (
                <div key={product.id} className="col-md-6">
                  <div className="card h-100 shadow-sm">
                    <div className="card-body">
                      <h5 className="card-title">{product.title}</h5>
                      <Link
                        href={`/products/${product.id}`}
                        className="btn btn-primary stretched-link"
                      >
                        <i className="bi bi-eye me-2"></i>Посмотреть
                      </Link>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {products.length > 0 && (
            <nav aria-label="Page navigation" className="mt-4">
              <ul className="pagination justify-content-center">
                <li className={`page-item ${!pagination.has_prev ? 'disabled' : ''}`}>
                  <Link
                    href={pagination.has_prev ? `/products?page=${pagination.prev_page}` : '#'}
                    className="page-link"
                  >
                    Предыдущая
                  </Link>
                </li>
                {Array.from({ length: pagination.total_pages }, (_, i) => i + 1).map(page => (
                  <li
                    key={page}
                    className={`page-item ${page === pagination.current_page ? 'active' : ''}`}
                  >
                    <Link href={`/products?page=${page - 1}`} className="page-link">
                      {page}
                    </Link>
                  </li>
                ))}
                <li className={`page-item ${!pagination.has_next ? 'disabled' : ''}`}>
                  <Link
                    href={pagination.has_next ? `/products?page=${pagination.next_page}` : '#'}
                    className="page-link"
                  >
                    Следующая
                  </Link>
                </li>
              </ul>
            </nav>
          )}
        </div>
      </div>
    </div>
  );
}