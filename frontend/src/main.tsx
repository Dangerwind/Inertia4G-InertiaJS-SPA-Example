import "vite/modulepreload-polyfill";
import { createRoot } from "react-dom/client";
import { createInertiaApp } from "@inertiajs/react";
import { InertiaProgress } from '@inertiajs/progress';
import React from 'react';

document.addEventListener('DOMContentLoaded', () => {
    InertiaProgress.init();

    createInertiaApp({
        resolve: (name) => {
            switch (name) {
                case 'About':
                    return import('./pages/About.tsx');
                case 'Index':
                    return import('./pages/Index.tsx');
                case 'ProductDetail':
                    return import('./pages/ProductDetail.tsx');
                case 'ProductsList':
                    return import('./pages/ProductsList.tsx');
                case 'CreateProduct':
                    return import('./pages/CreateProduct.tsx');
                default:
                    throw new Error(`Page ${name} not found`);
            }
        },
        setup({ el, App, props }: {
            el: HTMLElement,
            App: React.ComponentType<{ page: any }>,
            props: any
        }) {
            const root = createRoot(el);
            root.render(<App {...props} />);
        },
    });
});