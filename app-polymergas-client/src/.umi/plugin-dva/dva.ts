import { Component } from 'react';
import { ApplyPluginsType } from 'umi';
import dva from 'dva';
// @ts-ignore
import createLoading from 'D:/PDP/Projects/app-polymergas/app-polymergas-client/node_modules/dva-loading/dist/index.esm.js';
import { plugin, history } from '../core/umiExports';

let app:any = null;

export function _onCreate(options = {}) {
  const runtimeDva = plugin.applyPlugins({
    key: 'dva',
    type: ApplyPluginsType.modify,
    initialValue: {},
  });
  app = dva({
    history,
    
    ...(runtimeDva.config || {}),
    // @ts-ignore
    ...(typeof window !== 'undefined' && window.g_useSSR ? { initialState: window.g_initialProps } : {}),
    ...(options || {}),
  });
  
  app.use(createLoading());
  
  (runtimeDva.plugins || []).forEach((plugin:any) => {
    app.use(plugin);
  });
  app.model({ namespace: 'app', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/models/app.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/category/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/dashboard/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/exploiter/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/marketing/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/notification/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/order/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/products/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/productType/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/admin/sale/model.tsx').default) });
app.model({ namespace: 'model', ...(require('D:/PDP/Projects/app-polymergas/app-polymergas-client/src/pages/login/model.tsx').default) });
  return app;
}

export function getApp() {
  return app;
}

export class _DvaContainer extends Component {
  constructor(props: any) {
    super(props);
    // run only in client, avoid override server _onCreate()
    if (typeof window !== 'undefined') {
      _onCreate();
    }
  }

  componentWillUnmount() {
    let app = getApp();
    app._models.forEach((model:any) => {
      app.unmodel(model.namespace);
    });
    app._models = [];
    try {
      // 释放 app，for gc
      // immer 场景 app 是 read-only 的，这里 try catch 一下
      app = null;
    } catch(e) {
      console.error(e);
    }
  }

  render() {
    const app = getApp();
    app.router(() => this.props.children);
    return app.start()();
  }
}
