//
//  UIViewCotrollerExtensions.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 27.06.2022.
//

import Foundation
import UIKit
import shared

extension UIViewController {
//    func showErrorView(_ errorView: ErrorView!, error: RequestError) {
//        var image: UIImage
//        var color: UIColor
//        var buttonTitle: String
//
//        switch error {
//        case .noRepositories:
//            image = UIImage(named: "empty")!
//            color = UIColor(named: "DefaultBlue")!
//            buttonTitle = NSLocalizedString("errorView.reloadButton.refresh.title", comment: "")
//        case .noInternet:
//            image = UIImage(named: "connectionError")!
//            color = UIColor(named: "ErrorRed")!
//            buttonTitle = NSLocalizedString("errorView.reloadButton.retry.title", comment: "")
//        default:
//            image = UIImage(named: "somethingError")!
//            color = UIColor(named: "ErrorRed")!
//            buttonTitle = NSLocalizedString("errorView.reloadButton.retry.title", comment: "")
//        }
//
//        errorView.setUI(
//            image: image, description: error.errorDescription!,
//            suggestion: error.recoverySuggestion!,
//            color: color
//        )
//        errorView.isHidden = false
//    }
    
//    func hideErrorView(_ errorView: ErrorView!){
//        errorView.isHidden = true
//    }
    
    func showAlert(title: String, msg: String, sender: UIView) {
        let alertController = UIAlertController(
            title: title,
            message: msg,
            preferredStyle: .alert
        )
        
        let cancelAction = UIAlertAction(
            title: NSLocalizedString("errorAlert.okButton.title", comment: ""),
            style: .cancel, handler: nil
        )
        alertController.addAction(cancelAction)
        
        alertController.popoverPresentationController?.sourceView = sender
        present(alertController, animated: true, completion: nil)
    }
    
    func showAlert(for error: CustomError, sender: UIView){
        var msg: String
        
        if error is CustomError.UnknownRequest || error is CustomError.Unknown {
            msg = "\(error.failureReason!)"
        } else {
            msg = "\(error.errorDescription!)\n\(error.recoverySuggestion!)"
        }
        
        showAlert(title: NSLocalizedString("errorAlert.title", comment: ""), msg: msg, sender: sender)
    }
    
    func setExitButton(){
        let exitButton = UIBarButtonItem(
            image: UIImage(named: "exit"),
            style: UIBarButtonItem.Style.plain,
            target: self, action: #selector(self.onExitTapped)
        )
        exitButton.tintColor = UIColor.white
        navigationItem.setRightBarButton(exitButton, animated: true)
    }
    
    @IBAction func onExitTapped() {
        DI.shared.storage.authToken = nil
        navigationController?.setViewControllers([AuthViewController()], animated: true)
    }
}
