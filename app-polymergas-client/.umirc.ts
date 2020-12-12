import {defineConfig,utils} from 'umi';
import {resolve} from "path";

const { winPath } = utils;
export default defineConfig({



  nodeModulesTransform: {
    type: 'none',
  },

  antd: {},
  dva: {
    hmr: true,
  },
  locale: {
    default: 'zh-CN',
    baseNavigator: true,
  },
  // dynamicImport: {
  //   // 无需 level, webpackChunkName 配置
  //   // loadingComponent: './components/PageLoading/index'
  //   loading: '@/components/PageLoading/index',
  // },
  // 暂时关闭
  pwa: false,
  lessLoader: { javascriptEnabled: true },
  // cssLoader: {
  //   // 这里的 modules 可以接受 getLocalIdent
  //   modules: {
  //     getLocalIdent:(
  //       context: {
  //         resourcePath: string;
  //       },
  //       _: string,
  //       localName: string,
  //     ) => {
  //       if (
  //         context.resourcePath.includes('node_modules') ||
  //         context.resourcePath.includes('ant.design.pro.less') ||
  //         context.resourcePath.includes('global.less')
  //       ) {
  //         return localName;
  //       }
  //       const match = context.resourcePath.match(/src(.*)/);
  //       if (match && match[1]) {
  //         const antdProPath = match[1].replace('.less', '');
  //         const arr = winPath(antdProPath)
  //           .split('/')
  //           .map((a: string) => a.replace(/([A-Z])/g, '-$1'))
  //           .map((a: string) => a.toLowerCase());
  //         return `antd-pro${arr.join('-')}-${localName}`.replace(/--/g, '-');
  //       }
  //       return localName;
  //     },
  //   }
  // },

  proxy: {
    "/api": {
      "target": "http://localhost/",
      "changeOrigin": true
    },
    "/upload": {
      "target": "http://localhost/",
      "changeOrigin": true
    }
  },

  // dynamicImport: {
  //   // 无需 level, webpackChunkName 配置
  //   // loadingComponent: './components/PageLoading/index'
  //   loading: '@/components/PageLoading/index',
  // },
  // 暂时关闭

  alias: {
    api: resolve(__dirname, './src/services/'),
    components: resolve(__dirname, './src/components'),
    utils: resolve(__dirname, './src/utils')
  },

});
