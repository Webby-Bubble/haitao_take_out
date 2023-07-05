function getUserList (params) {
    return $axios({
        url: '/user/page',
        method: 'get',
        params
    })
}
// 修改---启用禁用接口
function enableOrDisableUser (params) {
    return $axios({
        url: '/user',
        method: 'put',
        data: { ...params }
    })
}