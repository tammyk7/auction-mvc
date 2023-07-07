import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/thrones': {
        target: 'https://thronesapi.com/api/v2/',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/thrones/, ''),
      },
    },
  },
})
