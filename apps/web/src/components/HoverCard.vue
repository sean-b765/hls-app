<script setup lang="ts">
import { Card, CardContent, CardHeader } from '@/components/ui/card'
</script>

<template>
  <Card class="h-80 bg-muted rounded-lg group transition-all duration-250 hover:p-1">
    <div
      class="bg-card rounded-lg flex flex-col w-full h-full relative cursor-pointer transition-all duration-250 group-hover:rounded-md"
    >
      <slot name="prepend-header" />
      <CardHeader
        v-if="$slots.header"
        class="p-6 pb-0 flex flex-row gap-2 items-center transition-all duration-250 group-hover:px-5 group-hover:pt-5"
      >
        <slot name="header" />
      </CardHeader>
      <CardContent
        class="p-6 flex grow items-center justify-center transition-all duration-250 group-hover:px-5 group-hover:pb-5"
        :class="{ 'group-hover:pt-5': !$slots.header }"
      >
        <slot name="content" />
      </CardContent>
    </div>
  </Card>
</template>
