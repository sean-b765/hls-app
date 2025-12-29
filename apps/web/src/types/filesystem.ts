export type FolderNode = {
  name: string
  path: string
  hasChildren: boolean
  children: FolderNode[]
  expanded?: boolean
  loading?: boolean
}
