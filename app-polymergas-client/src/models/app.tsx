// @ts-ignore
import {model} from '@/utils/model'
// @ts-ignore
import modelExtend from 'dva-model-extend'
// @ts-ignore
import api from "api"
import {history} from 'umi';
// @ts-ignore
import {STORAGE_NAME} from 'utils/constant';
import {toast} from "react-toastify";
// @ts-ignore
// import {STORAGE_NAME} from "../utils/constant";
const {getUserUnreadNotifications, getUsersBySortOrSearch, disableAndEnableUser, getUsers, getMainColors, getProduct, deleteProduct, changeProductPriceHaveStatus, getColors, saveProduct, getColorOnlyInMainColor, getColorInMainColor, signIn, userMe, uploadAttachment, switchToNew} = api


function pagination(page: number, numPages: number) {
  var res = [];
  var from = 1;
  var to = numPages;
  if (numPages > 10) {
    from = Math.max(page - 2, 1);
    to = Math.max(Math.min(page + 2, numPages), 5);
    if (to > 5) {
      res.push(1);
      if (from > 2) res.push(2);
      if (from > 3) {
        if (from === 4) {
          res.push(3);
        } else {
          res.push("...");
        }
      }
    }
  }
  for (var i = from; i <= to; i++) {
    res.push(i);
  }

  if (numPages > 10) {
    if (to < (numPages - 2)) {
      if (to === 8) {
        res.push(9);
      } else {
        res.push("...");
      }
    }
    if (to < numPages)
      res.push(numPages - 1);
    if (to !== numPages)
      res.push(numPages);
  }
  return res;
}


export default modelExtend(model, {
  namespace: "app",
  state: {
    currentUser: {},
    users: [],
    products: [],
    totalElements: 0,
    colors: [],
    maincolors: [],
    paginations: [],
    onlyInMainColor: [],
    inMainColor: [],
    pathname: "",
    historyPathname: "",
    totalPages: 0,
    page: 0,
    size: 10,
    admin: false,
    // totalElements: 0,
    modalShow: false,
    unReadNotifications: [],
  }, subscriptions: {
    setupHistory({dispatch, history}: { dispatch: any, history: any }) {
      history.listen((location: any) => {
        dispatch({
          type: 'updateState',
          payload: {
            locationPathname: location.pathname,
            locationQuery: location.query,
          },
        });
        dispatch({
          type: 'userMe',
          payload: {
            locationPathname: location.pathname,
            locationQuery: location.query,
          },
        })
      })
    },


  },
  effects: {
    * userMe({payload}: { payload: any }, {call, put, select}: any) {
      if (localStorage.getItem("polymergas-token")) {
        try {
          const res = yield call(userMe, payload);
          if (!res.success) {
            yield put({
              type: 'updateState',
              payload: {currentUser: {}}
            });
            localStorage.removeItem(STORAGE_NAME);
            history.push("/");
          } else {
            if (res.enabled) {
              yield put({
                type: 'updateState',
                payload: {
                  currentUser: res,
                  admin: res.roles ? res.roles.filter((res: { name: string; }) => res.name === "ROLE_ADMIN").length > 0 : false,
                }
              });
              if (!location.pathname.includes("/admin")) {
                history.push("/admin/dashboard");
              }
              yield put({
                type: 'getUserUnreadNotifications',
              });
              const go = true;
              return go
            } else {
              yield put({
                type: 'updateState',
                payload: {currentUser: {}}
              });
              localStorage.removeItem(STORAGE_NAME);
              history.push("/");
            }
          }
        } catch (error) {
          yield put({
            type: 'updateState',
            payload: {currentUser: {}}
          });
          localStorage.removeItem(STORAGE_NAME);
          history.push("/");
        }
      } else {
        history.push("/");
      }
    },
    * signIn({payload}: { payload: any }, {call, put, select}: any) {
      const res = yield call(signIn, payload);
      if (res.success) {
        toast.success('Login tasdiqlandi !')
        localStorage.setItem(STORAGE_NAME, res.body.tokenType + " " + res.body.accessToken);
        yield put({
          type: 'updateState',
          payload: {currentUser: res.body.accessToken}
        });
        history.push("/admin/dashboard");
        const enable = true;
        return enable
      } else {
        if (res.statusCode == '401') {
          toast.error('Parol yoki loginda xatolik');
        } else {
          toast.error(res.message);
        }
        const enable = false;
        return enable
      }
    },
    * uploadFile({payload}: { payload: any }, {put, call, select}: any) {
      yield put({
        type: "updateState",
        payload: {
          loading: true
        }
      })
      const data = yield call(uploadAttachment, payload);
      yield put({
        type: "updateState",
        payload: {
          loading: false
        }
      })
      if (data.success) {
        toast.success('Saqlandi')
        yield put({
          type: "updateState",
          payload: {
            attachmentId: data.object[0].fileId
          }
        });
        return data.object[0].fileId
      } else {
        toast.error('Xatolik yuz berdi !')
      }
    },
    * getUserUnreadNotifications({payload}: any, {call, put, select}: any) {
      const res = yield call(getUserUnreadNotifications, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            unReadNotifications: res.object.content,
            page: res.object.currentPage,
            // paginations: pagination(res.object.currentPage, res.object.totalPages)
          }
        })
      }
      return res;
    },
    * getUsersBySortOrSearch({payload}: any, {call, put, select}: any) {
      const res = yield call(getUsersBySortOrSearch, payload);
      if (res.success) {
        yield put({
          type: "updateState",
          payload: {
            users: res.object
          }
        })
        // toast.success(data.message)
      } else {
        toast.error(res.message);
      }
      return res
    },
    * saveProduct({payload}: { payload: any }, {call, put, select}: any) {
      const res = yield call(saveProduct, payload);
      yield put({
        type: "updateState",
        payload: {
          loading: true
        }
      });
      if (res.success) {
        toast.success('Saqlandi')
        yield put({
          type: "getProduct",
          payload: {
            page: 0, size: 10
          }
        });
      } else {
        toast.error('Xatolik yuz berdi !')
      }
      return res
    },
    * getProduct({payload}: { payload: any }, {call, put, select}: any) {
      const res = yield call(getProduct, payload);
      yield put({
        type: "app/updateState",
        payload: {
          loading: true
        }
      });
      yield put({
        type: "updateState",
        payload: {
          products: res.object.content,
          totalElements: res.object.totalElements
        }
      });
      return res
    },
    * getProductsForSale({payload}:{payload:any},{call,put,select}:any){
      const res = yield call(api.getProductsForSale,payload);
      yield put({
        type: "app/updateState",
        payload: {
          loading: true
        }
      });
      yield put({
        type: "updateState",
        payload: {
          products: res.object
        }
      });
      return res
    }
    ,
    * changeProductPriceHaveStatus({payload}: { payload: any }, {call, put, select}: any) {
      const res = yield call(changeProductPriceHaveStatus, payload);
      if (res.success) {
        yield put({
          type: 'getProduct',
          payload: {
            products: {
              page: 0,
              size: 10
            }
          }
        })
      }
      return res;
    },
    * delProduct({payload}: { payload: any }, {call, put, select}: any) {
      const res = yield call(deleteProduct, payload);
      if (res.success) {
        toast.success('Saqlandi')
        yield put({
          type: "getProduct",
          payload: {
            page: 0, size: 10
          }
        });
      }
    },
    * getMainColors({payload}: {payload: {nameUz: string, nameRu: string, nameEn: string,hexCode: string, colorCode: string}}, {call, put, select}: any) {
      const res = yield call(getMainColors, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            maincolors: res.object.content,
            page: 0,
            size: 10,
            totalElements: res.object.totalElements
          }
        })
      }
      return res;
    },
    * getUsers({payload}: {
      payload: {
        fullName: string, phoneNumber: string,
        enabled: boolean
      }
    }, {call, put, select}: any) {
      const res = yield call(getUsers, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            users: res.object.content,
          }
        })
      }
      return res;
    },
    * switchToNew({payload}: { payload: any }, {call, put, select}: any) {
      const res = yield call(switchToNew, payload);
      if (res.success) {
        toast.success('Tasdiqlandi !')
        return true
      } else {
        toast.error('Tasdiqlanmadi !')
        return false
      }
    },
    * getColors({payload}: {
      payload: {
        nameUz: string, nameRu: string, nameEn: string,
        hexCode: string, colorCode: string, mainColorIds: number[]
      }
    }, {call, put, select}: any) {
      const res = yield call(getColors, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            colors: res.object,
          }
        })
      }
      return res
    },
    * getColorOnlyInMainColor({payload}: {
      payload: {
        mainColorIds: number[],
        colorId: number[], totalPages: number, size: number, totalElements: number
      }
    }, {call, put, select}: any) {
      const res = yield call(getColorOnlyInMainColor, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            onlyInMainColor: res.object.content,
            totalElements: res.totalElements,
            page: res.object.pageable.pageNumber,
            totalPages: res.object.totalElements
          }
        })
      }
      return res
    },
    * getColorInMainColor({payload}: {
      payload: {
        mainColorIds: number[],
        colorId: number[], totalPages: number, size: number, totalElements: number
      }
    }, {call, put, select}: any) {
      const res = yield call(getColorInMainColor, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            onlyInMainColor: res.object.content,
            totalElements: res.totalElements,
            page: res.object.pageable.pageNumber,
            totalPages: payload && payload.totalPages ? payload.totalPages : 0
          }
        })
      }
      return res
    },

  }
})
