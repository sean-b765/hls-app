import BaseLayout from '@/layouts/BaseLayout.vue'
import HomeView from '@/views/HomeView.vue'
import LibrariesView from '@/views/LibrariesView.vue'
import LibraryDetailView from '@/views/LibraryDetailView.vue'
import { LayoutGrid, LibrarySquare } from 'lucide-vue-next'
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: BaseLayout,
    children: [
      {
        path: '',
        component: HomeView,
        name: 'Home',
        meta: {
          icon: LayoutGrid,
        },
      },
      {
        path: 'libraries',
        component: LibrariesView,
        name: 'Libraries',
        meta: {
          icon: LibrarySquare,
        },
      },
      {
        path: 'libraries/:id',
        component: LibraryDetailView,
        name: 'Library',
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
