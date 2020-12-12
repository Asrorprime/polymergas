namespace defaults {
  export interface apiResone {
    message: string,
    success: boolean
  };

  export interface apiResoneModel {
    message: string,
    success: boolean,
    object: any
  };

  export interface apiResoneModel {
    message: string,
    success: boolean,
    object: any
  };

  export interface apiResoneModelPage {
    message: string,
    success: boolean,
    object: {
      content: any,
      empty: boolean,
      first: boolean,
      last: boolean,
      number: number,
      numberOfElements: number,
      pageable: {
        sort: {
          sorted: boolean,
          unsorted: boolean,
          empty: boolean,
        },
        offset: number,
        pageNumber: number,
        pageSize: number,
        paged: boolean,
        unpaged: boolean
      }
      size: number,
      sort: {
        sorted: true,
        unsorted: boolean,
        empty: boolean,
      }
      totalElements: number,
      totalPages: number,
    }
  };


}
export const defaultFunction = (dispatch: any, modelName = "app", urlApiName: string, objectName: any | undefined, objectType: any, payload: any, successMessage: string | undefined, errorMessage: undefined | string) => {

  return
}

