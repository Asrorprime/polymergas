export default {
    //auth
    signIn: 'POST /auth/login',
    userMe: '/auth/me',
    searchUser: 'POST /user/searchuser',

    //category
    createEditMainColor: 'POST /color/main',
    createOrEditColor: 'POST /color',
    getMainColors: '/color/main',
    getColors: '/color',
    getColorOnlyInMainColor: 'POST /color/onlymainin',
    getColorInMainColor: 'POST /color/inmaincolor',
    deleteMainColor: 'DELETE /color/main',
    deleteColor: 'DELETE /color',

    //file
    uploadAttachment: 'POST /file',

    // Products
    saveProduct: 'POST /product',
    getProduct: '/product',
    getProductsForSale: '/product/productsForSale',
    changeProductPriceHaveStatus: '/product/changeProductPriceHaveStatus',
    deleteProduct: 'DELETE /product',
    switchToNew: '/product/switchToNew',
    saveStock: 'POST /product/sale',
    getStock: '/product/sale',
    delstock: 'DELETE /product/sale',


    //orders
    crateOrder: 'POST /userOrder',
    getOrders: '/userOrder/orderHistories',
    changeToPayed: 'PUT /userOrder/changeToPayed',
    changeOrderSum: 'PUT /userOrder/changeOrderSum',
    changeStepUserOrder: 'POST /userOrder/changeStep',
    // Products
    getProductsByColorId: 'GET /product/byColorId',
    saveNews: 'POST /news',
    getNews: '/news',
    deleteNews: 'DELETE /news',
    getAllNews: '/news',

    // Users
    getUsers: '/user',
    deleteUser: 'DELETE /user',
    addAdditionalPhone: '/user/addAdditionalPhone',
    disableAndEnableUser: '/user/disableUser',
    editUser: 'PUT /user/edit',
    addToAdmin: '/user/addToAdmin',
    editPassword: 'PUT /user/changePassword',
    editUserName: 'PUT /user/changePhoneNumber',
    getUsersBySortOrSearch: '/user/bySortOrSearch',

    // Dashboard
    getDashboardInfo: 'POST /report',
    // Dashboard


//Notification
    getNotification: '/notification',
    getNotifications: '/notification',
    readNotification: '/notification/readNotification',
    getUserUnreadNotifications: '/notification/unReadNotifications',
    deleteNotification: 'DELETE /notification',

    saveCategory: 'POST /category',
    getCategories: '/category',
    deleteCategory: 'DELETE /category',
    getSearchedCategories: '/category',

    saveProductType: 'POST /productType',
    getProductTypes: '/productType',
    deleteProductType: 'DELETE /productType',
    getSearchedProductTypes: '/productType',


    //Sale
    saveSale: 'POST /sale',
    getSale: '/sale',
    getParticipants: '/sale/participants',
    deleteSale: 'DELETE /sale',
    changeIsActive: '/sale/switchActiveSale',


    //marketing
    saveMarketing: 'POST /marketing',
    getMarketing: '/marketing',
    deleteMarketing: 'DELETE /marketing'
}
