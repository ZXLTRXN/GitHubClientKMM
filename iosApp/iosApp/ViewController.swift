//
//  ViewController.swift
//  iosApp
//
//  Created by Ilya Shevtsov on 09.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import shared

class ViewController: UIViewController {
    @IBOutlet weak var test: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .red
        
        let repo = AppRepository()
        repo.signIn(token: "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"){ [weak self] error in
            if let error = error {
                var message: String
                guard let kotlinExc = error.asKotlin() else {
                    return
                }

                if kotlinExc is CustomError.Unauthorized {
                    message = "unAuth"
                } else {
                    message = "else"
                }
                self?.test.text = message
                return
            }
            
            
            self?.test.text = "all right"
        }
        

    }
    
}


