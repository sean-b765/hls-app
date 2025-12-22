<!-- 
  File contains the component that displays Navbar.
  Nothing much has been done, I couldn't come up
  with an efficient way to connect the carousel
  component and Navbar component to I've been
  avoiding it lol
  
-->

<script setup lang="ts">
import {ButtonGroup} from '@/components/ui/button-group'
import { Button } from '@/components/ui/button'
</script>

<template>
  <div class="flex flex-col gap-4">>
    <ButtonGroup aria-label="MenuNav">
      <Button>Recommended</Button>
      <Button>Top 10 Movies</Button>
      <Button>Top 10 Series</Button>
    </ButtonGroup>
  </div>
</template>