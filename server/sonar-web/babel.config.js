module.exports = {
  compact: true,
  presets: [
    [
      '@babel/preset-env',
      {
        modules: false,
        targets: {
          browsers: [
            'last 2 Chrome versions',
            'last 2 Firefox versions',
            'last 2 Safari versions',
            'last 2 Edge versions'
          ]
        },
        corejs: '3.6',
        useBuiltIns: 'entry'
      }
    ],
    '@babel/preset-react'
  ],
  plugins: [['@babel/plugin-proposal-object-rest-spread', { useBuiltIns: true }], 'lodash'],
  env: {
    test: {
      plugins: [
        '@babel/plugin-transform-modules-commonjs',
        'dynamic-import-node',
        '@babel/plugin-transform-react-jsx-source',
        '@babel/plugin-transform-react-jsx-self'
      ]
    }
  }
};
