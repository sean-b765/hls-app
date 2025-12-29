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
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { ButtonGroup } from '@/components/ui/button-group'
import { useLibraryStore } from '@/stores/library'
import { getLibraryIcon } from '@/lib/utils'
import { ref } from 'vue'
import { cloneDeep, isEqual } from 'lodash'
import { Spinner } from '@/components/ui/spinner'
import { Library, LibraryTypeEnum } from '@hls-app/sdk'

const open = defineModel<boolean>('open')
const library = defineModel<Library>('library')
const loading = ref(false)
const typeOptions: LibraryTypeEnum[] = ['MOVIES', 'TV', 'MUSIC', 'ANIME', 'OTHER']
const libraryStore = useLibraryStore()

const createLibrarySchema = toTypedSchema(
  z.object({
    name: z
      .string()
      .min(2, { error: '2 characters minimum' })
      .max(50)
      .default(library.value?.name ?? ''),
    type: z.string().default(library.value?.type ?? ''),
  }),
)

async function editLibrary(values: unknown) {
  const edits = values as Partial<Library>
  if (!library.value) return

  const payload = cloneDeep(library.value)
  Object.assign(payload, edits)

  if (isEqual(payload, library.value)) {
    open.value = false
    return
  }

  loading.value = true
  await libraryStore.upsert(payload)
  loading.value = false

  open.value = false
}
</script>

<template>
  <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="createLibrarySchema">
    <Dialog v-model:open="open">
      <DialogContent class="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle>Edit library</DialogTitle>
        </DialogHeader>
        <form id="editLibraryForm" @submit="handleSubmit($event, editLibrary)">
          <FieldGroup>
            <VeeField v-slot="{ componentField, errors }" name="name">
              <Field :data-invalid="!!errors.length">
                <FieldLabel for="name"> Name </FieldLabel>
                <Input id="name" type="text" placeholder="Library name" v-bind="componentField" />
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
                    type="button"
                    variant="secondary"
                    class="shadow-none cursor-pointer"
                    :class="componentField.modelValue === option ? 'bg-ring hover:bg-ring' : ''"
                    @click="() => componentField['onUpdate:modelValue']!(option)"
                  >
                    <component :is="getLibraryIcon(option)" />
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
                          <component :is="getLibraryIcon(componentField.modelValue)" />
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
                        @click="() => componentField['onUpdate:modelValue']!(option)"
                      >
                        {{ getLibraryIcon(option) }}
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
          <Button
            class="mt-6 cursor-pointer"
            type="submit"
            size="sm"
            form="editLibraryForm"
            :disabled="loading"
          >
            <Spinner v-if="loading" />
            Save
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  </Form>
</template>
