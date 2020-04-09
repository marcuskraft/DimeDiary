module.exports = {
  devServer: {
    port: 4200,
    proxy: {
      '^/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    },
    watchOptions: {
      ignored: [/node_modules/]
    },
    disableHostCheck: true
  },
  lintOnSave: process.env.NODE_ENV !== 'production'
};
