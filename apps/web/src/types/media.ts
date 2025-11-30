export type ScanProgress = {
  [id: string]: Progress
}

export type Progress = 'METADATA' | 'INFO' | 'READY'
