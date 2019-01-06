var path = require('path')

module.exports = {
  entry: ['babel-polyfill', './js/index.js'],
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist')
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /(node_modules)/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['env', 'react'],
            plugins: ['react-html-attrs', 'transform-object-rest-spread']
          }
        }
      },
      {
        test: /\.css$/,
        use: [ 'style-loader', 'css-loader' ]
      }
    ]
  },
  devServer: {
    port: 9000,
    historyApiFallback: {
      disableDotRule: true
    }
  }
}
