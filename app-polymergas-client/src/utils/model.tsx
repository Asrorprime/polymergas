export const model = {
  reducers: {
    // @ts-ignore
    updateState(state, {payload}) {
      return {
        ...state,
        ...payload,
      }
    },
  }
};
