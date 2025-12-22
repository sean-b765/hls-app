<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod'
import { Field as VeeField } from 'vee-validate'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { useUserStore } from '@/stores/user'
import { ref } from 'vue'
import z from 'zod'
import { Form } from 'vee-validate'
import { Field, FieldError, FieldGroup, FieldLabel } from '@/components/ui/field'
import { AuthRequest } from '@hls-app/sdk'

const open = defineModel<boolean>('open')
const user = useUserStore()

const authFormSchema = toTypedSchema(
  z.object({
    username: z.string().min(4).max(50).default(''),
    password: z
      .string()
      .min(8, { error: '8 characters minimum' })
      .max(30, { error: '30 characters maximum' })
      .refine((password) => /[A-Z]/.test(password), {
        message: 'Atleast 1 uppercase character needed',
      })
      .refine((password) => /[a-z]/.test(password), {
        message: 'Atleast 1 lowercase character needed',
      })
      .refine((password) => /[0-9]/.test(password), { message: 'Atleast 1 number needed' })
      .refine((password) => /[!@#$%^&*]/.test(password), {
        message: 'Atleast 1 special character needed (!@#$%^&*)',
      })
      .default(''),
  }),
)

async function signin(values: unknown) {
  const authRequest = values as AuthRequest
  if (!authRequest || !authRequest.username || !authRequest.password) return
  const { username, password } = authRequest
  await user.signin(username, password)
  open.value = false
}
</script>

<template>
  <Form v-slot="{ handleSubmit }" as="" keep-values :validation-schema="authFormSchema">
    <Dialog :open="open">
      <DialogContent class="sm:max-w-[425px] [&>button]:hidden">
        <DialogHeader>
          <DialogTitle>Sign in</DialogTitle>
          <DialogDescription>Please sign in below.</DialogDescription>
        </DialogHeader>
        <form id="authForm" @submit="handleSubmit($event, signin)">
          <FieldGroup>
            <VeeField v-slot="{ componentField, errors }" name="username">
              <Field :data-invalid="!!errors.length">
                <FieldLabel for="username"> Username </FieldLabel>
                <Input
                  :default-value="''"
                  id="username"
                  type="text"
                  placeholder="Username"
                  v-bind="componentField"
                />
                <FieldError v-if="errors.length" :errors="errors.map((message) => ({ message }))" />
              </Field>
            </VeeField>
          </FieldGroup>

          <FieldGroup class="mt-4">
            <VeeField v-slot="{ componentField, errors }" name="password">
              <Field :data-invalid="!!errors.length">
                <FieldLabel for="password"> Password </FieldLabel>
                <Input
                  :default-value="''"
                  id="password"
                  type="password"
                  placeholder="********"
                  v-bind="componentField"
                />
                <FieldError v-if="errors.length" :errors="errors.map((message) => ({ message }))" />
              </Field>
            </VeeField>
          </FieldGroup>
        </form>
        <DialogFooter>
          <Button type="submit" form="authForm">Sign in</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  </Form>
</template>
