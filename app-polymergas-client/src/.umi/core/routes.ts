import { ApplyPluginsType } from 'D:/PDP/Projects/app-polymergas/app-polymergas-client/node_modules/@umijs/runtime';
import { plugin } from './plugin';

const routes = [
  {
    "path": "/",
    "component": require('@/layouts/index.tsx').default,
    "routes": [
      {
        "path": "/admin/category/AddCategory",
        "exact": true,
        "component": require('@/pages/admin/category/AddCategory.tsx').default
      },
      {
        "path": "/admin/category/delete",
        "exact": true,
        "component": require('@/pages/admin/category/delete.tsx').default
      },
      {
        "path": "/admin/category",
        "exact": true,
        "component": require('@/pages/admin/category/index.tsx').default
      },
      {
        "path": "/admin/dashboard",
        "exact": true,
        "component": require('@/pages/admin/dashboard/index.tsx').default
      },
      {
        "path": "/admin/exploiter",
        "exact": true,
        "component": require('@/pages/admin/exploiter/index.tsx').default
      },
      {
        "path": "/admin/marketing/addMarketing/addMarketing",
        "exact": true,
        "component": require('@/pages/admin/marketing/addMarketing/addMarketing.tsx').default
      },
      {
        "path": "/admin/marketing/delete",
        "exact": true,
        "component": require('@/pages/admin/marketing/delete.tsx').default
      },
      {
        "path": "/admin/marketing",
        "exact": true,
        "component": require('@/pages/admin/marketing/index.tsx').default
      },
      {
        "path": "/admin/notification",
        "exact": true,
        "component": require('@/pages/admin/notification/index.tsx').default
      },
      {
        "path": "/admin/order",
        "exact": true,
        "component": require('@/pages/admin/order/index.tsx').default
      },
      {
        "path": "/admin/products/addProduct",
        "exact": true,
        "component": require('@/pages/admin/products/addProduct.tsx').default
      },
      {
        "path": "/admin/products/Comfirm",
        "exact": true,
        "component": require('@/pages/admin/products/Comfirm.tsx').default
      },
      {
        "path": "/admin/products/delete",
        "exact": true,
        "component": require('@/pages/admin/products/delete.tsx').default
      },
      {
        "path": "/admin/products",
        "exact": true,
        "component": require('@/pages/admin/products/index.tsx').default
      },
      {
        "path": "/admin/productType/AddProductType",
        "exact": true,
        "component": require('@/pages/admin/productType/AddProductType.tsx').default
      },
      {
        "path": "/admin/productType/delete",
        "exact": true,
        "component": require('@/pages/admin/productType/delete.tsx').default
      },
      {
        "path": "/admin/productType",
        "exact": true,
        "component": require('@/pages/admin/productType/index.tsx').default
      },
      {
        "path": "/admin/sale/addSale/addSale",
        "exact": true,
        "component": require('@/pages/admin/sale/addSale/addSale.tsx').default
      },
      {
        "path": "/admin/sale/delete",
        "exact": true,
        "component": require('@/pages/admin/sale/delete.tsx').default
      },
      {
        "path": "/admin/sale",
        "exact": true,
        "component": require('@/pages/admin/sale/index.tsx').default
      },
      {
        "path": "/",
        "exact": true,
        "component": require('@/pages/index.tsx').default
      },
      {
        "path": "/login",
        "exact": true,
        "component": require('@/pages/login/index.tsx').default
      }
    ]
  }
];

// allow user to extend routes
plugin.applyPlugins({
  key: 'patchRoutes',
  type: ApplyPluginsType.event,
  args: { routes },
});

export { routes };
