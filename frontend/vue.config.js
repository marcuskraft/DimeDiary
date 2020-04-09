module.exports = {
  "devServer": {
    "port": 4200,
    "proxy": {
      "^/api": {
        "target": "http://localhost:8080",
        "changeOrigin": true,
        "secure": false
      }
    },
    "watchOptions": {
      "ignored": [
        {}
      ]
    },
    "disableHostCheck": true
  },
  "lintOnSave": true,
  "transpileDependencies": [
    "vuetify"
  ]
}