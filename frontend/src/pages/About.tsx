import React from 'react';
import { Link } from '@inertiajs/react';

export default function About({ description }) {
  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6">
          <div className="card shadow-sm">
            <div className="card-header bg-primary text-white">
              <h1 className="h4 mb-0">О нас</h1>
            </div>
            <div className="card-body">
              <p className="lead">{description}</p>
              <Link href="/" className="btn btn-outline-primary">
                <i className="bi bi-house me-2"></i>На главную
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}