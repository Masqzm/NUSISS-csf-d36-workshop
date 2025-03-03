import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { FileUploadService } from '../fileupload.service';

@Component({
  selector: 'app-upload',
  standalone: false,
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css'
})
export class UploadComponent implements OnInit {
  protected form!: FormGroup

  dataUri!: string
  blob!: Blob

  constructor(private router:Router, private fb:FormBuilder, private fileUploadSvc:FileUploadService) {}

  ngOnInit(): void {
    this.createForm()
  }
  createForm() {
    this.form = this.fb.group({
      comments: this.fb.control<string>('')
    })
  }

  onFileSelected($event: Event) {
    const input = $event.target as HTMLInputElement

    // If there is file stored in HTMLInputElement ("temp" dir) and size > 0
    if(input.files && input.files.length > 0) {
      const file = input.files[0]
      console.info(">>> File: ", file)

      // Read file as string (Base64) - Note: runs asynchronously
      const reader = new FileReader()
      reader.onload = () => {
        this.dataUri = reader.result as string
      }
      reader.readAsDataURL(file)
    }
  }

  upload() {
    console.info(">>> Uploaded an image")
    console.info(this.dataUri)
    
    if(!this.dataUri)
      return

    this.blob = this.dataURItoBlob(this.dataUri)
    const formValue = this.form.value

    this.fileUploadSvc.upload(formValue, this.blob).then(result => {
      console.info('>>> result: ', result)
      this.router.navigate(['/image', result.postId])
    })
  }

  dataURItoBlob(dataUri: string): Blob {
    const [meta, base64Data] = dataUri.split(',')
    const mimeMatch = meta.match(/:(.*?);/)

    const mimeType = mimeMatch ? mimeMatch[1] : 'application/octet-stream'
    const byteString = atob(base64Data)
    const ab = new ArrayBuffer(byteString.length)
    const ia = new Uint8Array(ab)
    for(let i = 0; i < byteString.length; i++)
      ia[i] = byteString.charCodeAt(i)

    return new Blob([ia], {type: mimeType})
  }
}
