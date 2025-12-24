<template>
  <button
    role="button"
    class="cursor-pointer p-1 rounded-full border-2 border-muted shadow-xs right-4 top-4 opacity-70 ring-offset-background transition-opacity hover:opacity-100 focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:pointer-events-none data-[state=open]:bg-accent data-[state=open]:text-muted-foreground"
  >
    <slot />
  </button>
</template>
