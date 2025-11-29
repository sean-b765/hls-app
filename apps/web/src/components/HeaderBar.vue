<script setup lang="ts">
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbSeparator,
} from '@/components/ui/breadcrumb'
import { Separator } from '@/components/ui/separator'
import { SidebarTrigger } from '@/components/ui/sidebar'
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { uniq } from 'lodash'
const route = useRoute()
const router = useRouter()

const breadcrumbs = computed(() => {
  const routesAccumulated: string[] = []
  route.path.split('/').forEach((path, index) => {
    if (index === 0) {
      routesAccumulated.push('/')
      return
    }
    const previousPath = routesAccumulated.at(index - 1) ?? ''
    const accumulatedPath = `${previousPath}/${path}`.replace(/\/{2,}/g, '/')
    routesAccumulated.push(accumulatedPath)
  })

  return uniq(routesAccumulated)
    .map((r) => router.resolve(r))
    .filter(Boolean)
    .map((r) => ({
      name: r.name as string,
      path: r.path,
      meta: r.meta,
    }))
})
</script>

<template>
  <header class="flex h-16 shrink-0 items-center gap-2">
    <div class="flex items-center gap-2 px-4">
      <SidebarTrigger class="-ml-4 -mt-1" />
      <transition
        enter-from-class="opacity-0"
        enter-active-class="transition-opacity duration-500"
        emter-to-class="opacity-100"
      >
        <Separator
          v-if="breadcrumbs.length"
          orientation="vertical"
          class="mr-2 data-[orientation=vertical]:h-4"
        />
      </transition>
      <Breadcrumb>
        <BreadcrumbList>
          <transition-group
            enter-from-class="opacity-0"
            enter-to-class="opacity-100"
            enter-active-class="transition-all duration-200 ease-out"
            leave-from-class="opacity-100"
            leave-to-class="opacity-0"
            leave-active-class="transition-all duration-200 ease-in"
          >
            <template v-for="(path, i) of breadcrumbs" :key="path">
              <BreadcrumbItem class="hidden md:block" :style="{ transitionDelay: `${i * 50}ms` }">
                <RouterLink :to="path.path" class="flex gap-1 items-center">
                  <component v-if="path.meta?.icon" :is="path.meta.icon" :size="12" />
                  <BreadcrumbLink tag="#"> {{ path.name }} </BreadcrumbLink>
                </RouterLink>
              </BreadcrumbItem>
              <BreadcrumbSeparator v-if="i !== breadcrumbs.length - 1" class="hidden md:block">
                <span class="text-xs">/</span>
              </BreadcrumbSeparator>
            </template>
          </transition-group>
        </BreadcrumbList>
      </Breadcrumb>
    </div>
  </header>
</template>
