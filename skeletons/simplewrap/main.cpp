/*
  JSmooth: a VM wrapper toolkit for Windows
  Copyright (C) 2003 Rodrigo Reyes <reyes@charabia.net>

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

*/

#include <windows.h>
#include <iostream>

#include "resource.h"


#include "common.h"
#include "ResourceManager.h"
#include "JVMRegistryLookup.h"
#include "JavaMachineManager.h"

void SaveJarResource();

/*  Declare Windows procedure  */
LRESULT CALLBACK WindowProcedure (HWND, UINT, WPARAM, LPARAM);

/*  Make the class name into a global variable  */
char szClassName[ ] = "JSmooth";

ResourceManager* globalResMan;

void lastExit()
{
  delete globalResMan;
  globalResMan = 0;
}

// void debug(std::string text)
// {
// }

// void _debugOutputCheck(std::string text)
// {
//   debug(text);
// }
DebugConsole DEBUGCONSOLE("JSmooth Debug");

void _debugOutput(const std::string& text)
{
  DEBUGCONSOLE.writeline(text);
}

void _debugWaitKey()
{
  DEBUGCONSOLE.waitKey();
}
                          
int WINAPI WinMain (HINSTANCE hThisInstance,
                    HINSTANCE hPrevInstance,
                    LPSTR lpszArgument,
                    int nFunsterStil)
{
    HWND hwnd;               /* This is the handle for our window */
    MSG messages;            /* Here messages to the application are saved */
    WNDCLASSEX wincl;        /* Data structure for the windowclass */

//    jlist->run();
    
    atexit(lastExit);
    //    debugOutput = debug;
    
    /* The Window structure */
    wincl.hInstance = hThisInstance;
    wincl.lpszClassName = szClassName;
    wincl.lpfnWndProc = WindowProcedure;      /* This function is called by windows */
    wincl.style = CS_DBLCLKS | CS_HREDRAW | CS_VREDRAW;                 /* Catch double-clicks */
    wincl.cbSize = sizeof (WNDCLASSEX);

    /* Use default icon and mouse-pointer */
    wincl.hIcon = LoadIcon (NULL, IDI_APPLICATION);
    wincl.hIconSm = LoadIcon (NULL, IDI_APPLICATION);
    wincl.hCursor = LoadCursor (NULL, IDC_ARROW);
    wincl.lpszMenuName = NULL;                 /* No menu */
    wincl.cbClsExtra = 0;                      /* No extra bytes after the window class */
    wincl.cbWndExtra = 0;                      /* structure or the window instance */
    /* Use Windows's default color as the background of the window */
    wincl.hbrBackground = (HBRUSH) COLOR_BACKGROUND;

    /* Register the window class, and if it fails quit the program */
    if (!RegisterClassEx (&wincl))
        return 0;

    /* The class is registered, let's create the program*/

    hwnd = CreateWindowEx (
           0,                   /* Extended possibilites for variation */
           szClassName,         /* Classname */
           "Windows App",       /* Title Text */
           WS_OVERLAPPEDWINDOW, /* default window */
           CW_USEDEFAULT,       /* Windows decides the position */
           CW_USEDEFAULT,       /* where the window ends up on the screen */
           544,                 /* The programs width */
           375,                 /* and height in pixels */
           HWND_DESKTOP,        /* The window is a child-window to desktop */
           NULL,                /* No menu */
           hThisInstance,       /* Program Instance handler */
           NULL                 /* No Window Creation data */
           );

    globalResMan = new ResourceManager("JAVA", PROPID, JARID);
    DEBUG(string("Main class: ") + globalResMan->getMainName());

    // sets up the command line arguments
    // not sure if lpszArgument can be null on Windows...
     if ((lpszArgument!=NULL) && (strlen(lpszArgument)>0))
       {
 	// Note that this overwrites an existing KEY_ARGUMENTS
 	globalResMan->setProperty(string(ResourceManager::KEY_ARGUMENTS), lpszArgument);
       }

    char curdir[256];
    GetCurrentDirectory(256, curdir);
    DEBUG(string("Currentdir: ") + curdir);

    string newcurdir = globalResMan->getProperty(ResourceManager::KEY_CURRENTDIR);
    SetCurrentDirectory(newcurdir.c_str());

    ShowWindow(hwnd, SW_HIDE);
    UpdateWindow(hwnd);

    JavaMachineManager man(*globalResMan);
    std::string preferDLLstr = globalResMan->getProperty("skel_SingleProcess");

    bool preferDLL = false;
    if (StringUtils::parseInt(preferDLLstr) > 0)
        preferDLL = true;
    if (man.run(true, preferDLL) == false)
    {
        std::string errmsg = globalResMan->getProperty("skel_Message");
        std::string url = globalResMan->getProperty("skel_URL");
        if (MessageBox(hwnd, errmsg.c_str(), "No Java?", MB_OKCANCEL|MB_ICONQUESTION|MB_APPLMODAL) == IDOK)
        {
            ShellExecute(hwnd, "open", url.c_str(), NULL, "", 0);
        }
    }

    //    MessageBox(hwnd, "Launched java", "DEBUG", MB_OKCANCEL|MB_ICONQUESTION|MB_APPLMODAL);

    DEBUG("NORMAL EXIT");
    DEBUGWAITKEY();

    /* Make the window visible on the screen */

//        ShowWindow (hwnd, nFunsterStil);
//        UpdateWindow(hwnd);
    /* Run the message loop. It will run until GetMessage() returns 0 */
//     while (GetMessage (&messages, NULL, 0, 0) > 0)
//     {
//         /* Translate virtual-key messages into character messages */
//         TranslateMessage(&messages);
//         /* Send message to WindowProcedure */
//         DispatchMessage(&messages);
//     }

//    MessageBox(hwnd, "AFTER GETMESSAGE", "DEBUG", MB_OKCANCEL|MB_ICONQUESTION|MB_APPLMODAL);

    /* The program return-value is 0 - The value that PostQuitMessage() gave */
    return messages.wParam;
}


/*  This function is called by the Windows function DispatchMessage()  */

LRESULT CALLBACK WindowProcedure (HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    HDC hdc;
	PAINTSTRUCT ps;

    char buffer[255];    
    char * mesg = "............................";
    mesg = buffer;

    switch (message)                  /* handle the messages */
    {
		case WM_PAINT:
			hdc = BeginPaint(hwnd, &ps);
			// TODO: Add any drawing code here...
			RECT rt;
			GetClientRect(hwnd, &rt);
			DrawText(hdc, mesg, 10, &rt, DT_CENTER);
			EndPaint(hwnd, &ps);
			break;

        case WM_DESTROY:
            PostQuitMessage (0);       /* send a WM_QUIT to the message queue */
            break;
        default:                      /* for messages that we don't deal with */
            return DefWindowProc (hwnd, message, wParam, lParam);
    }

    return 0;
}
