"use client"
import { useEffect, useState } from 'react';
import TaskNav from './TaskNav';
export default function RootLayout({ children }) {
 
  return (
    <html lang="en" >
        
        <body>
          <TaskNav/>
          {children}

        </body>
    </html>
  );
}
