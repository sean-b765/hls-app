<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod'
import { Field as VeeField } from 'vee-validate'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import z from 'zod'
import { Form } from 'vee-validate'
import { Field, FieldError, FieldGroup, FieldLabel } from '@/components/ui/field'
import { Clapperboard, JapaneseYen, Music, Plus, Shapes, TvMinimal } from 'lucide-vue-next'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { ButtonGroup } from '@/components/ui/button-group'
import { LibraryType } from '@/types/libraries'

const open = defineModel<boolean>('open')
const typeOptions: LibraryType[] = ['MOVIES', 'TV', 'MUSIC', 'ANIME', 'OTHER']

const createLibrarySchema = toTypedSchema(
  z.object({
    name: z.string().min(2, { error: '2 characters minimum' }).max(50).default(''),
    type: z.string().default('MOVIES'),
  }),
)

function createLibrary() {}
</script>

<template>
  <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="createLibrarySchema">
    <Dialog v-model:open="open">
      <DialogContent class="sm:max-w-[600px] [&>button]:hidden">
        <DialogHeader>
          <DialogTitle>Add a library</DialogTitle>
        </DialogHeader>
        <form id="addLibraryForm" @submit="handleSubmit($event, createLibrary)">
          <FieldGroup>
            <VeeField v-slot="{ componentField, errors }" name="name">
              <Field :data-invalid="!!errors.length">
                <FieldLabel for="name"> Name </FieldLabel>
                <Input
                  :default-value="''"
                  id="name"
                  type="text"
                  placeholder="Library name"
                  v-bind="componentField"
                />
                <FieldError v-if="errors.length" :errors="errors.map((message) => ({ message }))" />
              </Field>
            </VeeField>
          </FieldGroup>

          <FieldGroup class="mt-4">
            <VeeField v-slot="{ componentField, errors }" name="type">
              <Field :data-invalid="!!errors.length">
                <FieldLabel for="type"> Type </FieldLabel>
                <!-- Desktop/Tablet -->
                <ButtonGroup class="hidden sm:block" id="type">
                  <Button
                    v-for="option in typeOptions"
                    :key="option"
                    size="sm"
                    variant="secondary"
                    class="shadow-none cursor-pointer"
                    :class="
                      componentField.modelValue === option ? 'bg-primary hover:bg-primary' : ''
                    "
                    @click="() => (componentField.modelValue = option)"
                  >
                    <Clapperboard v-if="option === 'MOVIES'" />
                    <TvMinimal v-else-if="option === 'TV'" />
                    <Music v-else-if="option === 'MUSIC'" />
                    <JapaneseYen v-else-if="option === 'ANIME'" />
                    <Shapes v-else-if="option === 'OTHER'" />
                    {{ option }}
                  </Button>
                </ButtonGroup>
                <!-- Mobile -->
                <div class="flex">
                  <DropdownMenu class="block sm:hidden">
                    <DropdownMenuTrigger>
                      <div class="block sm:hidden">
                        <Button
                          id="type"
                          size="sm"
                          variant="secondary"
                          class="shadow-none cursor-pointer"
                        >
                          <Clapperboard v-if="componentField.modelValue === 'MOVIES'" />
                          <TvMinimal v-else-if="componentField.modelValue === 'TV'" />
                          <Music v-else-if="componentField.modelValue === 'MUSIC'" />
                          <JapaneseYen v-else-if="componentField.modelValue === 'ANIME'" />
                          <Shapes v-else-if="componentField.modelValue === 'OTHER'" />
                          {{ componentField.modelValue }}
                        </Button>
                      </div>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>
                      <DropdownMenuItem
                        v-for="option in typeOptions"
                        :key="option"
                        size="sm"
                        variant="secondary"
                        class="shadow-none cursor-pointer"
                        :class="
                          componentField.modelValue === option ? 'bg-primary hover:bg-primary' : ''
                        "
                        @click="() => (componentField.modelValue = option)"
                      >
                        <Clapperboard v-if="option === 'MOVIES'" />
                        <TvMinimal v-else-if="option === 'TV'" />
                        <Music v-else-if="option === 'MUSIC'" />
                        <JapaneseYen v-else-if="option === 'ANIME'" />
                        <Shapes v-else-if="option === 'OTHER'" />
                        {{ option }}
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </div>
                <FieldError v-if="errors.length" :errors="errors.map((message) => ({ message }))" />
              </Field>
            </VeeField>
          </FieldGroup>
        </form>
        <DialogFooter>
          <Button class="mt-6" type="submit" size="sm" form="addLibraryForm">
            <Plus />
            Create
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  </Form>
</template>
