import React from 'react';
import { useForm } from '@inertiajs/react';
import { Link } from '@inertiajs/react';

interface FormData {
  title: string;
  description: string;
}

interface Props {
  errors?: { [key: string]: string };
  title?: string;
  description?: string;
}

export default function CreateProduct({ errors: serverErrors, title: serverTitle, description: serverDescription }: Props) {
  const { data, setData, post, errors, processing } = useForm<FormData>({
    title: serverTitle || '',
    description: serverDescription || '',
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    // Отправляем как обычный JSON объект
    post('/products/create', {
      data: {  // ← Простой объект, не FormData!
        title: data.title,
        description: data.description
      },
      onSuccess: () => {
        console.log('Product created successfully');
        // Inertia автоматически обработает редирект на /products
      },
      onError: (errors) => {
        console.error('Form submission errors:', errors);
        // Ошибки попадут в useForm.errors автоматически
      },
    });
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6">
          <div className="card shadow-sm">
            <div className="card-header bg-success text-white">
              <h1 className="h4 mb-0">Добавить новый товар</h1>
            </div>
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="title" className="form-label">
                    Название товара
                  </label>
                  <input
                    type="text"
                    className={`form-control ${(errors.title || serverErrors?.title) ? 'is-invalid' : ''}`}
                    id="title"
                    name="title"
                    value={data.title}
                    onChange={(e) => setData('title', e.target.value)}
                    placeholder="Введите название"
                    required
                  />
                  {(errors.title || serverErrors?.title) && (
                    <div className="invalid-feedback">{errors.title || serverErrors?.title}</div>
                  )}
                </div>
                
                <div className="mb-3">
                  <label htmlFor="description" className="form-label">
                    Описание
                  </label>
                  <textarea
                    className={`form-control ${(errors.description || serverErrors?.description) ? 'is-invalid' : ''}`}
                    id="description"
                    name="description"
                    value={data.description}
                    onChange={(e) => setData('description', e.target.value)}
                    placeholder="Введите описание"
                    rows={5}
                  />
                  {(errors.description || serverErrors?.description) && (
                    <div className="invalid-feedback">{errors.description || serverErrors?.description}</div>
                  )}
                </div>
                
                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-success" disabled={processing}>
                    <i className="bi bi-plus-circle me-2"></i>
                    {processing ? 'Создание...' : 'Создать товар'}
                  </button>
                  <Link href="/products" className="btn btn-outline-secondary">
                    <i className="bi bi-arrow-left me-2"></i>К списку товаров
                  </Link>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
